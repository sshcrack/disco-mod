package me.sshcrack.disco_lasers.mixin;

import com.anthonyhilyard.prism.text.DynamicColor;
import com.anthonyhilyard.prism.util.IColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = DynamicColor.class, remap = false)
public interface DynamicColorAccessor {
    @Accessor("values")
    List<IColor> getValues();

    @Accessor("duration")
    float getDuration();
}
