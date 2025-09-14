package org.example;

import FileSystem.FileSystem;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try
        {
            FileSystem fileSystem = new FileSystem();
            fileSystem.writeData("key1", "value1");
            fileSystem.writeData("key2", "value2");
            System.out.println("Read data from file : " + fileSystem.getData("key2"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}