package eu.skillcraft.beckpatterns.preparation;

import org.apache.catalina.Realm;

public interface AuthPort {

  boolean isAuditor();

  AppUser getUser();
}
