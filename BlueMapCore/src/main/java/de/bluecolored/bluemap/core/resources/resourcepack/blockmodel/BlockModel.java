package de.bluecolored.bluemap.core.resources.resourcepack.blockmodel;

import de.bluecolored.bluemap.core.debug.DebugDump;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@DebugDump
public class BlockModel {

    private ResourcePath<BlockModel> parent;
    private Map<String, TextureVariable> textures = new HashMap<>();
    private List<Element> elements;
    private boolean ambientocclusion = true;

    private transient boolean liquid = false;
    private transient boolean culling = false;
    private transient boolean occluding = false;

    private BlockModel(){}

    @Nullable
    public ResourcePath<BlockModel> getParent() {
        return parent;
    }

    public Map<String, TextureVariable> getTextures() {
        return textures;
    }

    @Nullable
    public List<Element> getElements() {
        return elements;
    }

    public boolean isAmbientocclusion() {
        return ambientocclusion;
    }

    public boolean isLiquid() {
        return liquid;
    }

    public boolean isCulling() {
        return culling;
    }

    public boolean isOccluding() {
        return occluding;
    }

    public synchronized void applyParent(ResourcePack resourcePack) {
        if (this.parent == null) return;

        // set parent to null early to avoid trying to resolve reference-loops
        ResourcePath<BlockModel> parentPath = this.parent;
        this.parent = null;

        if (parentPath.getFormatted().equals("bluemap:builtin/liquid")) {
            this.liquid = true;
            return;
        }

        BlockModel parent = parentPath.getResource(resourcePack::getBlockModel);
        if (parent != null) {
            parent.applyParent(resourcePack);

            parent.textures.forEach(this.textures::putIfAbsent);
            if (this.elements == null) this.elements = parent.elements;
        }
    }

    public synchronized void calculateProperties(ResourcePack resourcePack) {
        if (elements == null) return;
        for (Element element : elements) {
            if (element.isFullCube()) {
                occluding = true;

                culling = true;
                for (Direction dir : Direction.values()) {
                    Face face = element.getFaces().get(dir);
                    if (face == null) {
                        culling = false;
                        break;
                    }

                    ResourcePath<Texture> textureResourcePath = face.getTexture().getTexturePath(textures::get);
                    if (textureResourcePath == null) {
                        culling = false;
                        break;
                    }

                    Texture texture = textureResourcePath.getResource(resourcePack::getTexture);
                    if (texture == null || texture.getColorStraight().a < 1) {
                        culling = false;
                        break;
                    }
                }

                break;
            }
        }
    }

}
