package de.fabiankrueger.archunit.customer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static de.fabiankrueger.archunit.customer.SpringPredicates.areAnnotatedWithSpringService;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

public class LayerConformityArchTest {

  public static final String WEB_LAYER_PACKAGE = "..web";
  public static final String SERVICE_LAYER_PACKAGE = "..service";
  public static final String API_LAYER_PACKAGE = "..api";

  @ArchTest
  public static final ArchRule testLayerAccess = layeredArchitecture()
      .layer("Web").definedBy(WEB_LAYER_PACKAGE)
      .layer("api").definedBy(API_LAYER_PACKAGE)
      .layer("Service").definedBy("..service")

      .whereLayer("Web").mayNotBeAccessedByAnyLayer()
      .whereLayer("api").mayOnlyBeAccessedByLayers("Web", "Service")
      .whereLayer("Service").mayNotBeAccessedByAnyLayer();

//  @ArchTest
//  public static final ArchRule controllersShouldResideInWebLayer =
//      classes()
//          .that(areAnnotatedWithSpringRestController)
//          .should()
//          .resideInAPackage(WEB_LAYER_PACKAGE);

  @ArchTest
  public static final ArchRule serviceClassesShouldResideInServiceLayer =
      classes()
          .that(areAnnotatedWithSpringService)
          .should()
          .resideInAPackage(SERVICE_LAYER_PACKAGE);

  @ArchTest
  public static final ArchRule classesInServiceLayerShouldNotBePublic =
      classes()
          .that()
          .resideInAPackage(SERVICE_LAYER_PACKAGE)
          .should()
          .notBePublic();

  @ArchTest
  public static final ArchRule apiClassesShouldBePublic =
      classes()
          .that()
          .resideInAPackage(API_LAYER_PACKAGE)
          .should()
          .bePublic();


}
