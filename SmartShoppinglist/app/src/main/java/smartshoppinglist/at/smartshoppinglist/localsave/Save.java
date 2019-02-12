package smartshoppinglist.at.smartshoppinglist.localsave;

import android.content.Context;
import android.util.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;

public class Save {

    public static synchronized void save(Context context, String[] keys, Object... values) throws IOException {

        String filepath = context.getFilesDir().getAbsolutePath()+"/db.json";
        Writer out = new FileWriter(filepath);
        JsonWriter writer = new JsonWriter(out);
        writer.beginArray();
        int i = 0;
        for(Object value : values){
            saveObject(writer, keys[i], value);
            i++;
        }
        writer.endArray();
        writer.close();
    }

    private static synchronized void saveObject(JsonWriter writer, String key, Object value) throws IOException {
        writer.beginObject();
        writer.name(key);
        if(value instanceof String){
            writer.value(String.valueOf(value));
        }
        else if(value instanceof Integer){
            writer.value((Integer) value);
        }
        writer.endObject();
    }
}
