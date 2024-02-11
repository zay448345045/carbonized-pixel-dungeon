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

package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.ColorBlock;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.PointerArea;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.particles.Emitter;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.windows.WndInfoTalent;

public class TalentButton extends Button {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 26;

	int tier;
	Talent talent;
	int pointsInTalent;
	boolean upgradeEnabled;

	TalentIcon icon;
	Image bg;

	ColorBlock fill;

	public TalentButton(int tier, Talent talent, int points, boolean upgradeEnabled){
		super();
		hotArea.blockLevel = PointerArea.NEVER_BLOCK;

		this.tier = tier;
		this.talent = talent;
		this.pointsInTalent = points;
		this.upgradeEnabled = upgradeEnabled;

		bg.frame(20*(talent.maxPoints()-1), 0, WIDTH, HEIGHT);

		icon = new TalentIcon( talent );
		add(icon);
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		fill = new ColorBlock(0, 4, 0xFFFFFF44);
		add(fill);

		bg = new Image(Assets.Interfaces.TALENT_BUTTON);
		add(bg);
	}

	@Override
	protected void layout() {
		width = WIDTH;
		height = HEIGHT;

		super.layout();

		fill.x = x+2;
		fill.y = y + WIDTH - 1;
		fill.size( pointsInTalent/(float)talent.maxPoints() * (WIDTH-4), 5);

		bg.x = x;
		bg.y = y;

		icon.x = x + 2;
		icon.y = y + 2;
		PixelScene.align(icon);
	}

	@Override
	protected void onClick() {
		super.onClick();

		if (upgradeEnabled
				&& Dungeon.hero != null
				&& Dungeon.hero.isAlive()
				&& Dungeon.hero.talentPointsAvailable(tier) > 0
				&& Dungeon.hero.pointsInTalent(talent) < talent.maxPoints()){
			CarbonizedPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, new Callback() {
				@Override
				public void call() {
					upgradeTalent();
				}
			}));
		} else {
			CarbonizedPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, null));
		}
	}

	@Override
	protected String hoverText() {
		return Messages.titleCase(talent.title());
	}

	@Override
	protected void onPointerDown() {
		icon.brightness( 1.5f );
		bg.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.Sounds.CLICK );
	}

	@Override
	protected void onPointerUp() {
		icon.resetColor();
		bg.resetColor();
	}

	public void enable( boolean value ) {
		active = value;
		icon.alpha( value ? 1.0f : 0.3f );
		bg.alpha( value ? 1.0f : 0.3f );
	}

	public void upgradeTalent(){
		if (Dungeon.hero.talentPointsAvailable(tier) > 0 && parent != null) {
			Dungeon.hero.upgradeTalent(talent);
			float oldWidth = fill.width();
			pointsInTalent++;
			layout();
			Sample.INSTANCE.play(Assets.Sounds.LEVELUP, 0.7f, 1.2f);
			Emitter emitter = (Emitter) parent.recycle(Emitter.class);
			emitter.revive();
			emitter.pos(fill.x + (fill.width() + oldWidth) / 2f, fill.y + fill.height() / 2f);
			emitter.burst(Speck.factory(Speck.STAR), 12);
		}
	}
}
