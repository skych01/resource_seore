package com.ch.resource_seore.service;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface PathHandleService {
    String storage(byte[] file, String project, String fileName) throws IOException;

    File out(String code) throws FileNotFoundException;
}
