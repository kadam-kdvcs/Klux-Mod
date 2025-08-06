package org.kdvcs.klux.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MeshUtil {

    public static int getMeshLevel(ItemStack mesh) {
        if (mesh.is(ModTags.Items.EXTRACTION_MESH_ULTIMATE)) return 3;
        if (mesh.is(ModTags.Items.EXTRACTION_MESH_ADVANCED)) return 2;
        if (mesh.is(ModTags.Items.EXTRACTION_MESH_BASIC)) return 1;
        return 0;
    }

    public static float getChanceMultiplier(ItemStack mesh) {
        switch (getMeshLevel(mesh)) {
            case 3: return 1.5f;
            case 2: return 1.2f;
            case 1: return 1.0f;
            default: return 1.0f;
        }
    }

    public static boolean canMeshMatchRecipe(ItemStack meshItem, Ingredient recipeMesh) {
        int meshLevel = getMeshLevel(meshItem);
        if (meshLevel == 0) return false;

        if (recipeMesh.isEmpty()) return true;

        for (ItemStack stack : recipeMesh.getItems()) {
            if (stack.isEmpty()) continue;

            if (stack.is(ModTags.Items.EXTRACTION_MESH_ULTIMATE) && meshLevel >= 3) return true;
            if (stack.is(ModTags.Items.EXTRACTION_MESH_ADVANCED) && meshLevel >= 2) return true;
            if (stack.is(ModTags.Items.EXTRACTION_MESH_BASIC) && meshLevel >= 1) return true;
        }

        return false;
    }
}
