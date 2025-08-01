package com.XHxinhe.aliveandwell.hometpaback;

import com.XHxinhe.aliveandwell.hometpaback.util.TextUtils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HomeComponent {
    private double x, y, z;
    private float pitch, yaw;
    private String name;
    private RegistryKey<World> dim;

    public HomeComponent(double x, double y, double z, float pitch, float yaw, RegistryKey<World>  dim, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.name = name;
        this.dim = dim;
    }

    public HomeComponent(Vec3d pos, float pitch, float yaw, RegistryKey<World>  dim, String name) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.name = name;
        this.dim = dim;
    }

    public double getX()  { return x; }
    public double getY()  { return y; }
    public double geyZ()  { return z; }
    public float getPitch()  { return pitch; }
    public float getYaw()    { return yaw;   }
    public String getName()   { return name;  }
    public RegistryKey<World>  getDimID() { return dim; }

    public MutableText toText(MinecraftServer server) {
        return Text.translatable("%s\n%s; %s; %s\n%s; %s\n%s",
                TextUtils.valueRepr("Name", name),
                TextUtils.valueRepr("X", x), TextUtils.valueRepr("Y", y), TextUtils.valueRepr("Z", z),
                TextUtils.valueRepr("Yaw", yaw), TextUtils.valueRepr("Pitch", pitch),
                TextUtils.valueRepr("In", dim.toString()));
    }
}
