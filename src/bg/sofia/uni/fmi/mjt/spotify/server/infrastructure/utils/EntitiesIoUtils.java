//package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.utils;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Map;
//
//public class EntitiesIoUtils {
//    private static <T> List<T> readEntitiesFromFileSystem(Class<T> tClass, Path sourceFile) {
//        try (var reader = new FileReader(sourceFile.toString())) {
//            Type type = TypeToken.getParameterized(List.class, tClass).getType();
//            List<T> res = new Gson().fromJson(reader, type);
//            if (res == null) {
//                return List.of();
//            }
//            return res;
//        } catch (IOException e) {
//            throw new RuntimeException("Cannot read entities");
//        }
//    }
//
//    public <T> void saveEntitiesToFileSystem(Path sourceFile, Map<String, T> entities) {
//        try (var writer = new FileWriter(sourceFile.toString())) {
//            new Gson().toJson(entities.values(), writer);
//        } catch (IOException e) {
//            throw new RuntimeException("Cannot read entities");
//        }
//    }
//}
