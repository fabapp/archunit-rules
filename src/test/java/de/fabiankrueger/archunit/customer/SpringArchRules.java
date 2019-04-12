package de.fabiankrueger.archunit.customer;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

interface SpringArchRules {

  ArchCondition<? super JavaMethod> followStrictSetterRules = new ArchCondition<JavaMethod>(
      "follow strict setter definition") {
    @Override
    public void check(JavaMethod javaMethod, ConditionEvents conditionEvents) {

      if (getNumberOfParameter(javaMethod) != 1) {
        conditionEvents.add(SimpleConditionEvent.violated(javaMethod,
            String.format("Method %s should have one parameter, but had %s.",
                javaMethod.getFullName(),
                getNumberOfParameter(javaMethod))));
      }

      if (false == javaMethod.getName().startsWith("set")) {
        conditionEvents.add(SimpleConditionEvent.violated(javaMethod,
            String.format("A setter should start with 'set' but method name is '%s'.",
                javaMethod.getName())));
      }

      String methodName = javaMethod.getName().replace("set", "");
      String firstLetter = methodName.substring(0, 1);
      final String memberName = methodName.replace(firstLetter, firstLetter.toLowerCase());

      if (javaMethod.getOwner().getFields().stream().noneMatch(field -> field.getName().equals(memberName))) {
        conditionEvents.add(SimpleConditionEvent.violated(javaMethod,
            String.format("method '%s' is a setter and a matching field '%s' should exist.",
                javaMethod.getName(),
                memberName)));
      }

      if (false == hasExactlyOneInstruction(javaMethod)) {
        conditionEvents.add(SimpleConditionEvent.violated(javaMethod,
            String.format("method '%s' is setter and should only set '%s', but contains '%s' more instructions.",
                javaMethod.getName(),
                memberName,
                javaMethod.getAccessesFromSelf().stream().collect(Collectors.toList()).size() - 1)));
      }
    }
  };

  static int getNumberOfParameter(JavaMethod javaMethod) {
    return javaMethod.getRawParameterTypes().size();
  }

  static boolean hasExactlyOneInstruction(JavaMethod javaMethod) {
    return javaMethod.getAccessesFromSelf().stream().collect(Collectors.toList()).size() == 1;
  }

  /**
   * Verify that all methods (not constructors) annotated with @Autowired begin with 'set' and have one parameter which
   * sets a member named after the method name (e.g. setFoo() sets foo) and nothing more.
   */
  ArchRule verifyMethodsAnnotatedWithAutowired =
      methods()
          .that()
          .areAnnotatedWith(Autowired.class)
          .should(followStrictSetterRules)
          .because(
              "Setters for spring dependencies should be conform to java bean spec. and never need to do anything than setting the field.");


  /**
   * Forbid member variables to be annotated with @Autowired.
   *
   * Reason: When @Autowired is attached to member variables without setter access the code is probably not unittested.
   * or fields are set using reflection which might be seen as a code smell.
   *
   * Solution: Either use Constructor injection or annotate setter with @Autowired and set field accordingly
   *
   * @see #verifyMethodsAnnotatedWithAutowired
   */
  ArchRule fieldsShouldNotBeAnnotatedWithAutowired =
      fields()
          .should().notBeAnnotatedWith(Autowired.class);
}