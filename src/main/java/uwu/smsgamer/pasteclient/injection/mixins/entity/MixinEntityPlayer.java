/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uwu.smsgamer.pasteclient.injection.mixins.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import uwu.smsgamer.pasteclient.injection.interfaces.IMixinEntityPlayer;
import uwu.smsgamer.pasteclient.modules.modules.movement.Speed;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase implements IMixinEntityPlayer {

    @Shadow
    protected float speedInAir;

    public MixinEntityPlayer(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @Shadow
    public abstract void addStat(StatBase p_addStat_1_);

    @Shadow
    public abstract void addExhaustion(float p_addExhaustion_1_);

    @Override
    public float getAirSpeed() {
        return speedInAir;
    }

    @Override
    public void setAirSpeed(float airSpeed) {
        speedInAir = airSpeed;
    }
}
