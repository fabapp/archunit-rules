package de.fabiankrueger.archunit.customer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;
import static de.fabiankrueger.archunit.customer.BasicConditions.haveADefaultConstructor;
import static de.fabiankrueger.archunit.customer.SpringPredicates.areAnnotatedWithSpringRestController;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaConstructor;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(
    packagesOf = CustomerModule.class,
    importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class ArchFitnessTest {

  /*
   * Spring managed beans should either be injected
   */

//  @ArchTest
//  public static final ArchRule someRule =
//      ArchRuleDefinition.classes().that().

  /*------------------------------------------------------
   * Example 1
   *------------------------------------------------------*/
  @ArchTest
  public static final ArchRule apiClassesShouldNotAccessAnyOtherPackages =
      ArchRuleDefinition
          .classes()
          .that()
          .resideInAPackage("..web..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..service..", "..web..");

  /*------------------------------------------------------
   * Example 2
   *------------------------------------------------------*/
  /**
   * Predicate for classes annotated with either @Entity or @Embeddable
   */
  private static DescribedPredicate<? super JavaClass> areJpaModelClasses =
      new DescribedPredicate<JavaClass>("are annotated with @Entity or @Embeddable") {
        @Override
        public boolean apply(JavaClass javaClass) {
          return javaClass.isAnnotatedWith(Entity.class) || javaClass.isAnnotatedWith(Embeddable.class);
        }
      };

  /**
   * Checks if given class has a default constructor.
   */
  public static ArchCondition<? super JavaClass> haveADefaultConstructor =
      new ArchCondition<JavaClass>("have default constructor") {

        @Override
        public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
          try {
            JavaConstructor javaConstructor = javaClass.getConstructor();
          } catch (Exception e) {
            conditionEvents.add(SimpleConditionEvent.violated(
                javaClass,
                String.format("Class %s should have a default constructor", javaClass.getFullName())));
          }
        }
      };

  /**
   * Test taht all classes annotated with either @Embeddable or @Entity should have a default constructor
   * @see de.fabiankrueger.archunit.customer.api.Customer
   */
  @ArchTest
  public static final ArchRule jpa_model_classes_should_have_default_cosntructor =
      classes()
          .that(areJpaModelClasses)
          .should(haveADefaultConstructor);


  @ArchTest
  public static final ArchRule fieldsShouldNotBeAnnotatedWithAutowired = SpringArchRules.fieldsShouldNotBeAnnotatedWithAutowired;

  @ArchTest
  public static final ArchRule onlySetterAnnotatedWithAutowired = SpringArchRules.verifyMethodsAnnotatedWithAutowired;

  @ArchTest
  public static final ArchRule testAgainstUml = classes()
      .should(adhereToPlantUmlDiagram(ArchFitnessTest.class.getResource("/my-diagram.puml"),
          consideringOnlyDependenciesInDiagram())
      );

  // consideringOnlyDependenciesInDiagram()) //consideringAllDependencies())

//    ArchConditions.onlyHaveDependenciesInAnyPackage()

  @ArchTest
  public static final ArchRule controllerShouldOnlyDependOnServices = classes()
      .that()
      .areAnnotatedWith(RestController.class)
      .or()
      .areAnnotatedWith("Controller")
      .should()
      .resideInAPackage("..playground..")
      .andShould()
      .haveSimpleNameEndingWith("Controller")
      .andShould()
      .onlyAccessClassesThat()
      .areAnnotatedWith(Service.class)
      .orShould()
      .accessClassesThat()
      .resideInAPackage("java.lang..")
//      .resideInAPackage("java.lang")
      ;

  @ArchTest
  public static final ArchRule services = classes()
      .that()
      .areAnnotatedWith(Service.class)
      .should()
      .haveSimpleNameEndingWith("Service")
      .andShould()
      .onlyAccessClassesThat()
      .areAnnotatedWith(Repository.class)
      .orShould()
      .onlyAccessClassesThat()
      .areAnnotatedWith(Service.class)
      .orShould()
      .accessClassesThat()
      .resideInAPackage("java.lang..");


  @ArchTest
  public static final ArchRule methods_in_restcontroller_should_have_mapping_annotation = methods()
      .that()
      .areDeclaredInClassesThat(areAnnotatedWithSpringRestController)
      .and()
      .arePublic()
      .should()
      .beAnnotatedWith(PostMapping.class)
      .orShould()
      .beAnnotatedWith(GetMapping.class)
      .orShould()
      .beAnnotatedWith(PutMapping.class)
      .orShould()
      .beAnnotatedWith(DeleteMapping.class);

  @ArchTest
  public static final ArchRule diPointsMustBeAccessible = fields()
      .should()
      .bePrivate();

//      .that()
//      .doNotHaveModifier()
//      .should()
//      .notBeAnnotatedWith("Autowired");


  public static final DescribedPredicate<JavaClass> ofTypeRepository = new DescribedPredicate<JavaClass>(
      "with 'Repository' superclass name") {
    @Override
    public boolean apply(JavaClass javaClass) {
      if (javaClass.getSuperClass().isPresent()) {
        return javaClass.getSuperClass().get().getName().contains("Repository");
      } else {
        return false;
      }
    }
  };

  @ArchTest
  public static final ArchRule memberOfTypeOrWithAnnotationShouldBeFinal =
      fields()
          .that()
          .haveRawType(ofTypeRepository)
          .should()
          .bePackagePrivate();

//          .haveModifier(JavaModifier.FINAL);


  @ArchTest
  public static final ArchRule r1 = JpaArchRules.jpaEntitiesMustHaveDefaultConstructor;

}
