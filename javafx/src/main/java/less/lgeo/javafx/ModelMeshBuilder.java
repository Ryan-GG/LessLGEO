package less.lgeo.javafx;

import javafx.scene.shape.TriangleMesh;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Triangle;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Vertex;

public class ModelMeshBuilder {
    public static TriangleMesh buildMesh(Model model) {
        TriangleMesh mesh = new TriangleMesh();
        // Add vertices
        for (Triangle t : model.getTriangleList()) {
            addVertex(mesh, t.getP1());
            addVertex(mesh, t.getP2());
            addVertex(mesh, t.getP3());
        }
        for (Quadrilateral q : model.getQuadrilateralList()) {
            addVertex(mesh, q.getP1());
            addVertex(mesh, q.getP2());
            addVertex(mesh, q.getP3());
            addVertex(mesh, q.getP4());
        }
        // Add dummy texture coordinates (required by TriangleMesh)
        mesh.getTexCoords().addAll(0,0);
        // Add faces for triangles
        int vertexIndex = 0;
        for (int i = 0; i < model.getTriangleList().size(); i++) {
            mesh.getFaces().addAll(vertexIndex, 0, vertexIndex+1, 0, vertexIndex+2, 0);
            vertexIndex += 3;
        }
        // Add faces for quadrilaterals (split into two triangles)
        for (int i = 0; i < model.getQuadrilateralList().size(); i++) {
            // Each quad: v0,v1,v2,v3
            int v0 = vertexIndex;
            int v1 = vertexIndex+1;
            int v2 = vertexIndex+2;
            int v3 = vertexIndex+3;
            // First triangle: v0,v1,v2
            mesh.getFaces().addAll(v0,0,v1,0,v2,0);
            // Second triangle: v2,v3,v0
            mesh.getFaces().addAll(v2,0,v3,0,v0,0);
            vertexIndex += 4;
        }
        return mesh;
    }
    private static void addVertex(TriangleMesh mesh, Vertex v) {
        mesh.getPoints().addAll((float)v.getX(), (float)v.getY(), (float)v.getZ());
    }
} 