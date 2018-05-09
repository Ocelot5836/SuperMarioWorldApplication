package com.ocelot.mod.game.main.entity.ai;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.ocelot.mod.game.core.entity.ai.AIBase;

public class AIBowser extends AIBase {

	private StopWatch timer;

	@Override
	public void initAI() {
		this.timer = StopWatch.createStarted();
	}

	@Override
	public void update() {
		if (this.timer.getTime(TimeUnit.MILLISECONDS) % 1500 == 0) {
			System.out.println(random.nextInt(5));
		}
	}

	@Override
	public String getName() {
		return "Bowser";
	}
}