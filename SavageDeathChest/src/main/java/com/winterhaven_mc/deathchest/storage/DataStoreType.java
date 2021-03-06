package com.winterhaven_mc.deathchest.storage;

import com.winterhaven_mc.deathchest.PluginMain;

enum DataStoreType {

	SQLITE("SQLite") {

		@Override
		public DataStore create() {
			
			// create new sqlite datastore object
			return new DataStoreSQLite(plugin);
		}
	};

	private final String displayName;

	private final static PluginMain plugin = PluginMain.instance;
	
	private final static DataStoreType defaultType = DataStoreType.SQLITE;
	
	public abstract DataStore create();
	

	/**
	 * Class constructor
	 * @param displayName the formatted display name of a datastore type
	 */
	DataStoreType(final String displayName) {
		this.displayName = displayName;
	}

	
	@Override
	public final String toString() {
		return this.displayName;
	}

	
	public static DataStoreType match(final String name) {
		for (DataStoreType type : DataStoreType.values()) {
			if (type.toString().equalsIgnoreCase(name)) {
				return type;
			}
		}
		// no match; return default type
		return defaultType;
	}
	
	
	public static DataStoreType getDefaultType() {
		return defaultType;
	}
}
