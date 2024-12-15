package me.sshcrack.disco_lasers.util;

import io.wispforest.endec.SerializationContext;
import io.wispforest.endec.Serializer;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;

public class SerializerWrapper<T> implements DataOutput {
    private final Serializer<T> serializer;
    private final SerializationContext context;

    public SerializerWrapper(SerializationContext context, Serializer<T> serializer) {
        this.serializer = serializer;
        this.context = context;
    }

    @Override
    public void write(int b) throws IOException {
        serializer.writeInt(context, b);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {

    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {

    }

    @Override
    public void writeBoolean(boolean v) throws IOException {

    }

    @Override
    public void writeByte(int v) throws IOException {

    }

    @Override
    public void writeShort(int v) throws IOException {

    }

    @Override
    public void writeChar(int v) throws IOException {

    }

    @Override
    public void writeInt(int v) throws IOException {

    }

    @Override
    public void writeLong(long v) throws IOException {

    }

    @Override
    public void writeFloat(float v) throws IOException {

    }

    @Override
    public void writeDouble(double v) throws IOException {

    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {

    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {

    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {

    }
}
