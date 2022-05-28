package de.bluecolored.bluemap.core.resources.adapter;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4d;
import com.flowpowered.math.vector.Vector4f;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.bluecolored.bluemap.core.resources.resourcepack.blockmodel.Face;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.math.Axis;
import de.bluecolored.bluemap.core.util.math.Color;

import java.util.EnumMap;

public class ResourcesGson {

    public static final Gson INSTANCE = createGson();

    private static Gson createGson() {

        return new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Axis.class, new AxisAdapter())
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .registerTypeAdapter(Direction.class, new DirectionAdapter())
                .registerTypeAdapter(Vector3d.class, new Vector3dAdapter())
                .registerTypeAdapter(Vector3f.class, new Vector3fAdapter())
                .registerTypeAdapter(Vector4d.class, new Vector4dAdapter())
                .registerTypeAdapter(Vector4f.class, new Vector4fAdapter())
                .registerTypeAdapter(
                        new TypeToken<EnumMap<Direction, Face>>(){}.getType(),
                        new EnumMapInstanceCreator<Direction, Face>(Direction.class)
                )
                .create();

    }

}