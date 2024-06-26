package satisfyu.herbalbrews.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ToughEffect extends MobEffect {
    public ToughEffect() {
        super(MobEffectCategory.NEUTRAL, 0x00FF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10.0), this::isAffectedEntity)
                .forEach(living -> applyEffects(living, amplifier));
    }

    private boolean isAffectedEntity(LivingEntity entity) {
        return entity.isAlive() && !(entity instanceof Player && ((Player) entity).isCreative());
    }

    private void applyEffects(LivingEntity entity, int amplifier) {
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, amplifier + 1));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, amplifier + 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20, amplifier + 1));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
