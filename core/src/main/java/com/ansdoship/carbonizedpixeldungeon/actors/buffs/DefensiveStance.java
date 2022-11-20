package com.ansdoship.carbonizedpixeldungeon.actors.buffs;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class DefensiveStance extends Buff {

    {
        type = buffType.POSITIVE;
    }

    public int pos = -1;

    @Override
    public boolean act() {
        if (pos == -1) pos = target.pos;
        if (pos != target.pos) {
            detach();
        } else {
            spend(TICK);
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.SHIELD;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1,1,0);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 4+4*Dungeon.hero.pointsInTalent(Talent.ENHANCED_SHIELD));
    }

    @Override
    public void detach() {
        defense(false);
        super.detach();
    }

    public void defense(boolean enabled) {
        Hero hero = Dungeon.hero;
        if (hero != null && hero.sprite != null) {
            if (enabled) {
                Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, Random.Float(1.05f, 1.2f) );
                hero.sprite.operate( hero.pos );
            }
            SpellSprite.show( hero, enabled ? SpellSprite.DEFENSE : SpellSprite.NO_DEFENSE );
        }
    }

}