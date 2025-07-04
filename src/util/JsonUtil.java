    package util;

    import com.fasterxml.jackson.databind.ObjectMapper;

    public class JsonUtil {
        private static final ObjectMapper mapper = new ObjectMapper();

        public static String toJson(Object obj) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                return "{}";
            }
        }

        public static <T> T fromJson(String json, Class<T> clazz) {
            try {
                return mapper.readValue(json, clazz);
            } catch (Exception e) {
                return null;
            }
        }

        public static String getQueryParam(String path, String key) {
            if (!path.contains("?")) return null;

            String[] parts = path.split("\\?");
            if (parts.length < 2) return null;

            String query = parts[1];
            String[] params = query.split("&");

            for (String param : params) {
                String[] kv = param.split("=");
                if (kv.length == 2 && kv[0].equals(key)) {
                    return kv[1];
                }
            }

            return null;
        }
    }
