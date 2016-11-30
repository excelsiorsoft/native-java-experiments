package com.excelsiorsoft.using_scanf_in_java;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Kernel32;

public class JNABucket {
	public static void main(String args[]) {

		JNAApiInterface jnaLib = JNAApiInterface.INSTANCE;
		Kernel32 klib = Kernel32.INSTANCE;
		Memory userName = new Memory(256);

		jnaLib.setbuf(jnaLib.stdout, null);
		jnaLib.printf("Hello World");
		
		for (int i = 0; i < args.length; i++) {
			jnaLib.printf("\nArgument %d : %s", i, args[i]);
		}

		jnaLib.puts("\nPlease Enter Your Name:\n");
		
		jnaLib.scanf("%s", userName);
		jnaLib.printf("\nYour name is: %s", userName);
		
		jnaLib.fprintf(jnaLib.stdout, "\nThis is a Test");

	}
}
