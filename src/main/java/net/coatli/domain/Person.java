package net.coatli.domain;

import static com.jsoniter.output.JsonStream.serialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain  = true,
           fluent = false)
public class Person {

  private String id;
  private String curp;
  private String names;
  private String firstSurname;
  private String secondSurname;
  private String gender;
  private String birthday;

  @Override
  public String toString() {
    return serialize(this);
  }

}
