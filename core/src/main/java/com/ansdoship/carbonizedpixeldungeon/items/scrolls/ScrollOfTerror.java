/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.ansdoship.carbonizedpixeldungeon.items.scrolls;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Terror;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.effects.Flare;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfTerror extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_TERROR;
	}

	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				new Flare( 5, 32 ).color( 0xFF0000, true ).show( curUser.sprite, 2f );
				Sample.INSTANCE.play( Assets.Sounds.READ );

				int count = 0;
				Mob affected = null;
				float duration = Terror.DURATION;
				if (curUser.subClass == HeroSubClass.LOREMASTER) duration = Math.round(duration * 1.5f);
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
						Buff.affect( mob, Terror.class, duration ).object = curUser.id();

						if (mob.buff(Terror.class) != null){
							count++;
							affected = mob;
						}
					}
				}

				switch (count) {
					case 0:
						GLog.i( Messages.get(ScrollOfTerror.this, "none") );
						break;
					case 1:
						GLog.i( Messages.get(ScrollOfTerror.this, "one", affected.name()) );
						break;
					default:
						GLog.i( Messages.get(ScrollOfTerror.this, "many") );
				}
				identify();

				readAnimation();

			}
		});

	}
	
	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}
}
