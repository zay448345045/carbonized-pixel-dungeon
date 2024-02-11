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

package com.tianscar.carbonizedpixeldungeon.noosa.tweeners;

import com.tianscar.carbonizedpixeldungeon.noosa.Visual;
import com.tianscar.carbonizedpixeldungeon.utils.PointF;

public class ScaleTweener extends Tweener {

	public Visual visual;
	
	public PointF start;
	public PointF end;
	
	public ScaleTweener( Visual visual, PointF scale, float time ) {
		super( visual, time );
		
		this.visual = visual;
		start = visual.scale;
		end = scale;
	}

	@Override
	protected void updateValues( float progress ) {
		visual.scale = PointF.inter( start, end, progress );
	}
}
