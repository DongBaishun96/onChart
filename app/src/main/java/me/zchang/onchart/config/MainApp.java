package me.zchang.onchart.config;

import android.app.Application;


/*
 *    Copyright 2015 Zhehua Chang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

public class MainApp extends Application {
	private ConfigManager configManager;

	@Override
	public void onCreate() {
		super.onCreate();

		configManager = new ConfigManager(this);

	}

	public ConfigManager getConfigManager() {
		return configManager;
	}
}
