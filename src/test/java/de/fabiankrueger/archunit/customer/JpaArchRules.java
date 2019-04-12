package de.fabiankrueger.archunit.customer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static de.fabiankrueger.archunit.customer.BasicConditions.haveADefaultConstructor;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import javax.persistence.Entity;

public class JpaArchRules {

  @ArchTest
  public static final ArchRule jpaEntitiesMustHaveDefaultConstructor = classes()
      .that()
      .areAnnotatedWith(Entity.class)
      .should(haveADefaultConstructor);

}
