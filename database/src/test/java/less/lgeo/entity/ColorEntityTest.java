package less.lgeo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import less.lgeo.common.Color;
import org.junit.jupiter.api.Test;

class ColorEntityTest {

  private static final Integer id = 1;
  private static final String name = "name";
  private static final String value = "#FFFFFF";
  private static final String edge = "#FFFFFF";
  private static final Integer alpha = 255;
  private static final Integer luminance = 200;
  private static final String finish = "CHROME";

  @Test
  void completeEntityToGpb() {

    ColorEntity entity = new ColorEntity( id, name, value, edge, alpha, luminance, finish );

    Color gpbColor = ColorEntity.toGpb( entity );

    assertEquals( entity.getId(), gpbColor.getId() );
    assertEquals( entity.getName(), gpbColor.getName() );
    assertEquals( entity.getValue(), gpbColor.getValue() );
    assertEquals( entity.getAlpha(), gpbColor.getAlpha() );
    assertEquals( entity.getLuminance(), gpbColor.getLuminance() );
    assertEquals( entity.getFinish(), gpbColor.getFinish() );
  }

  @Test
  void nullAlphaToGpb() {

    ColorEntity entity = new ColorEntity( id, name, value, edge, null, luminance, finish );

    Color gpbColor = ColorEntity.toGpb( entity );

    assertEquals( entity.getId(), gpbColor.getId() );
    assertEquals( entity.getName(), gpbColor.getName() );
    assertEquals( entity.getValue(), gpbColor.getValue() );
    assertNull( entity.getAlpha() );
    assertEquals( entity.getLuminance(), gpbColor.getLuminance() );
    assertEquals( entity.getFinish(), gpbColor.getFinish() );
  }

  @Test
  void nullLuminanceToGpb() {

    ColorEntity entity = new ColorEntity( id, name, value, edge, alpha, null, finish );

    Color gpbColor = ColorEntity.toGpb( entity );

    assertEquals( entity.getId(), gpbColor.getId() );
    assertEquals( entity.getName(), gpbColor.getName() );
    assertEquals( entity.getValue(), gpbColor.getValue() );
    assertEquals( entity.getAlpha(), gpbColor.getAlpha() );
    assertNull( entity.getLuminance() );
    assertEquals( entity.getFinish(), gpbColor.getFinish() );
  }

  @Test
  void nullFinishToGpb() {

    ColorEntity entity = new ColorEntity( id, name, value, edge, alpha, luminance, null );

    Color gpbColor = ColorEntity.toGpb( entity );

    assertEquals( entity.getId(), gpbColor.getId() );
    assertEquals( entity.getName(), gpbColor.getName() );
    assertEquals( entity.getValue(), gpbColor.getValue() );
    assertEquals( entity.getAlpha(), gpbColor.getAlpha() );
    assertEquals( entity.getLuminance(), gpbColor.getLuminance() );
    assertNull( entity.getFinish() );
  }

  @Test
  void nullIdToGpb() {

    ColorEntity entity = new ColorEntity( null, name, value, edge, alpha, luminance, finish );
    assertThrowsExactly( IllegalArgumentException.class, () -> ColorEntity.toGpb( entity ) );
  }
}