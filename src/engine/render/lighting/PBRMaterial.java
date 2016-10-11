package engine.render.lighting;

/**
 * Created by anarchist on 7/25/16.
 */
public class PBRMaterial {
    private float roughness;
    private float fresnel;
    private float specularPower;
    private float reflectivity;

    public PBRMaterial() {
        this.roughness = 0.1f;
        this.fresnel = 0.8f;
        this.specularPower = 0.0f;
        this.reflectivity = 0.0f;
    }

    public float getRoughness() {
        return roughness;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }

    public float getFresnel() {
        return fresnel;
    }

    public void setFresnel(float reflectivity) {
        this.fresnel = reflectivity;
    }

    public float getSpecularPower() {
        return specularPower;
    }

    public void setSpecularPower(float specularPower) {
        this.specularPower = specularPower;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
