package less.lgeo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import less.lgeo.common.Color;
import org.junit.jupiter.api.Test;

class ColorEntityTest {

  private static final Integer id = 1;
  private static final String name = "name";
  private static final String rgb = "FFFFFF";
  private static final boolean isTrans = false;
  private static final int numParts = 1;
  private static final int numSets = 1;
  private static final int y1 = 1;
  private static final int y2 = 1;


  @Test
  void completeEntityToGpb() {

    ColorEntity entity = new ColorEntity(id, name, rgb, isTrans, numParts, numSets, y1, y2);

    Color gpbColor = ColorEntity.toGpb(entity);

    assertEquals(entity.getId(), gpbColor.getId());
    assertEquals(entity.getName(), gpbColor.getName());
    assertEquals(entity.getRgb(), gpbColor.getRgb());
    assertEquals(entity.isTrans(), gpbColor.getIsTrans());
  }

  @Test
  void nullStartYearToGpb() {

    ColorEntity entity = new ColorEntity(id, name, rgb, isTrans, numParts, numSets, null, y2);

    Color gpbColor = ColorEntity.toGpb(entity);

    assertEquals(entity.getId(), gpbColor.getId());
    assertEquals(entity.getName(), gpbColor.getName());
    assertEquals(entity.getRgb(), gpbColor.getRgb());
    assertEquals(entity.isTrans(), gpbColor.getIsTrans());
  }

  @Test
  void nullEndYearToGpb() {

    ColorEntity entity = new ColorEntity(id, name, rgb, isTrans, numParts, numSets, y1, null);

    Color gpbColor = ColorEntity.toGpb(entity);

    assertEquals(entity.getId(), gpbColor.getId());
    assertEquals(entity.getName(), gpbColor.getName());
    assertEquals(entity.getRgb(), gpbColor.getRgb());
    assertEquals(entity.isTrans(), gpbColor.getIsTrans());
  }

  @Test
  void nullEntityToGpb() {

    ColorEntity entity = null;

    assertThrowsExactly(EntityToGpbConversionException.class, () -> ColorEntity.toGpb(entity),
        "ColorEntity was null");
  }

}