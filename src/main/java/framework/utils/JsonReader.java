package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

// Bài 4: JSON Reader và POJO Model
public class JsonReader {

    // Lớp POJO đại diện cho 1 JSON Object
    public static class UserData {
        public String username;
        public String password;
        public String role;
        public boolean expectSuccess;
    }

    public static List<UserData> readUsers(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<List<UserData>>() {});
    }
}