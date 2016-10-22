package com.util;

import javax.naming.Context;
import javax.naming.NamingException;

public class Lookup {

	public static <T, P> String getLookupName(Class<T> bean, Class<P> interfac) {
		String appName = "";
		String moduleName = "GerenciamentoDeAmbientesEJB";
		String distinctName = "";
		String beanName = bean.getSimpleName();
		final String interfaceName = interfac.getName();

		String name = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + interfaceName;
		// String name = "java:global/" + moduleName + "/" + beanName + "!" +
		// interfaceName;
		return name;
	}

	public static <T, P> Object doLookup(Class<T> bean, Class<P> interfac) {
		Object object = null;
		Context context = null;
		String lookupName;

		lookupName = Lookup.getLookupName(bean, interfac);
		try {
			context = ClientUtility.getInitialContext();
			return context.lookup(lookupName);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return object;
	}
}
