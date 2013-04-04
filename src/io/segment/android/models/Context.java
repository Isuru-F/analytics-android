package io.segment.android.models;


public class Context extends EasyJSONObject {

	private static final String IP_KEY = "ip";
	private static final String LIBRARY_KEY = "library";
	private static final String PROVIDERS_KEY = "providers";

	public Context() {
		super();
		addLibraryContext();
	}

	public Context(Object... kvs) {
		super(kvs);
		addLibraryContext();
	}

	private void addLibraryContext() {
		this.put(LIBRARY_KEY, "analytics-android");
	}

	public Context setIp(String ip) {
		this.put(IP_KEY, ip);
		return this;
	}

	public Context setProviders(Providers providers) {
		this.put(PROVIDERS_KEY, providers);
		return this;
	}

	public String getIp() {
		return (String) this.get(IP_KEY);
	}
	
	@Override
	public Context put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}