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

package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite.Glowing;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Ankh extends Item {

	public static final String AC_BLESS = "BLESS";

	{
		image = ItemSpriteSheet.ANKH;

		//You tell the ankh no, don't revive me, and then it comes back to revive you again in another run.
		//I'm not sure if that's enthusiasm or passive-aggression.
		bones = true;
	}

	private boolean blessed = false;
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		Waterskin waterskin = hero.belongings.getItem(Waterskin.class);
		if (waterskin != null && waterskin.isFull() && !blessed)
			actions.add( AC_BLESS );
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_BLESS )) {

			Waterskin waterskin = hero.belongings.getItem(Waterskin.class);
			if (waterskin != null){
				blessed = true;
				waterskin.empty();
				GLog.p( Messages.get(this, "bless") );
				hero.spend( 1f );
				hero.busy();


				Sample.INSTANCE.play( Assets.Sounds.DRINK );
				CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				hero.sprite.operate( hero.pos );
			}
		}
	}
	
	@Override
	public String desc() {
		if (blessed)
			return Messages.get(this, "desc_blessed");
		else
			return super.desc();
	}

	public boolean isBlessed(){
		return blessed;
	}

	public void bless(){
		blessed = true;
	}

	private static final Glowing WHITE = new Glowing( 0xFFFFCC );

	@Override
	public Glowing glowing() {
		return isBlessed() ? WHITE : null;
	}

	private static final String BLESSED = "blessed";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( BLESSED, blessed );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		blessed	= bundle.getBoolean( BLESSED );
	}
	
	@Override
	public int value() {
		return 50 * quantity;
	}
}
