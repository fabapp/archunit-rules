package de.fabiankrueger.archunit.customer;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

public class SpringPredicates {

  public static final DescribedPredicate<JavaClass> annotatedWithSpringAnnotations = new DescribedPredicate<JavaClass>(
      "annotated with @Service, @Repository or @Component") {

    List<Object> annotations = Arrays.asList(Service.class, Component.class, Repository.class);

    @Override
    public boolean apply(JavaClass javaClass) {

      return javaClass.getAnnotations().stream()
          .anyMatch(anno -> annotations.contains(anno.getRawType()));
    }
  };

  public static final DescribedPredicate<JavaClass> areAnnotatedWithSpringService = new DescribedPredicate<JavaClass>(
      "annotated with @Service") {
    @Override
    public boolean apply(JavaClass javaClass) {
      return javaClass.isAnnotatedWith(Service.class);
    }
  };
  public static final DescribedPredicate<JavaClass> annotatedWithSpringService = areAnnotatedWithSpringService;


  public static final DescribedPredicate<JavaClass> areAnnotatedWithSpringRestController = new DescribedPredicate<JavaClass>(
      "annotated with @RestController") {
    @Override
    public boolean apply(JavaClass javaClass) {
      return javaClass.isAnnotatedWith(RestController.class);
    }
  };
  public static final DescribedPredicate<JavaClass> annotatedWithSpringRestController = areAnnotatedWithSpringRestController;


  public static final DescribedPredicate<JavaClass> annotatedWithSpringComponent  = new DescribedPredicate<JavaClass>(
      "annotated with @Component") {
    @Override
    public boolean apply(JavaClass javaClass) {
      return javaClass.isAnnotatedWith(Component.class);
    }
  };
  public static final DescribedPredicate<JavaClass> areAnnotatedWithSpringComponent = annotatedWithSpringComponent;

}
