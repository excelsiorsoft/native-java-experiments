package com.excelsiorsoft.using_scanf_in_java;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

public interface JNAApiInterface extends Library {
    JNAApiInterface INSTANCE = (JNAApiInterface) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), JNAApiInterface.class);
    Pointer __iob_func();

    int sizeOfFileStructure = Platform.is64Bit() ? 48 : 32;
    Pointer stdout = JNAApiInterface.INSTANCE.__iob_func().share(sizeOfFileStructure);

    void printf(String format, Object... args);
    int sprintf(byte[] buffer, String format, Object... args);
    int scanf(String format, Object... args);
    int fflush (Pointer stream);
    int puts(String format) ;
    int fprintf(Pointer stream, String format, Object...args) ;
    void setbuf(Pointer stream, String buffer) ;
}