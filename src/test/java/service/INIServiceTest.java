package service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.bmarsaud.boxedroid.service.INIService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class INIServiceTest {
    private static Map<String, String> EXPECTED_MAP = new LinkedHashMap<String, String>() {{
        put("key1", "value1");
        put("key2", "value2");
    }};
    private static String EXPECTED_CONTENT = "key1=value1\nkey2=value2\n";
    private static String EXPECTED_MODIFIED_CONTENT = "key1=value1\nkey2=newValue\n";

    private File readFile;
    private File writeFile;
    private File editFile;

    @BeforeAll
    public void init() throws IOException {
        readFile = new File("read.ini");
        readFile.createNewFile();

        editFile = new File("edit.init");
        editFile.createNewFile();

        FileWriter writer = new FileWriter(readFile);
        writer.write(EXPECTED_CONTENT);
        writer.close();

        writer = new FileWriter(editFile);
        writer.write(EXPECTED_CONTENT);
        writer.close();
    }

    @Test
    public void read() throws IOException {
        Map<String, String> map = INIService.read(readFile);
        assertEquals(EXPECTED_MAP, map);
    }

    @Test
    public void write() throws IOException {
        writeFile = new File("write.ini");
        INIService.write(writeFile, EXPECTED_MAP);

        assertTrue(writeFile.exists());

        String fileContent = Files.lines(Paths.get(writeFile.getPath())).reduce("", (acc, value) -> acc + value + "\n");
        assertEquals(EXPECTED_CONTENT, fileContent);
    }

    @Test
    public void edit() throws IOException {
        Map<String, String> editMap = new HashMap<>();
        editMap.put("key2", "newValue");

        INIService.edit(editFile, editMap);

        String fileContent = Files.lines(Paths.get(editFile.getPath())).reduce("", (acc, value) -> acc + value + "\n");
        assertEquals(EXPECTED_MODIFIED_CONTENT, fileContent);
    }

    @AfterAll
    public void clean() {
        readFile.delete();
        writeFile.delete();
        editFile.delete();
    }
}
