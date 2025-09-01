package less.lgeo.entity;

import static less.lgeo.test.ModelTestUtils.cubeMesh;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import less.lgeo.primitive.Model;
import org.junit.jupiter.api.Test;

class ModelEntityTest {

  @Test
  void toEntity() {
    Model gpb = cubeMesh().build();

    ModelEntity entity = ModelEntity.toEntity(gpb);

    assertEquals(entity.getUuid().toString(), gpb.getUUID());
    assertArrayEquals(entity.getModelData(), gpb.toByteArray());
  }

  /*@Test
  void toGpb() throws InvalidProtocolBufferException {
    UUID id = UUID.randomUUID();
    Model gpb = pyramid().setUUID(id.toString()).build();

    ModelEntity entity = new ModelEntity(UUID.randomUUID(), gpb.toByteArray());
    Model converted = ModelEntity.toGpb(entity);

    assertEquals(gpb, converted);
  }*/

}
