import { Vector3 } from "three";
import { MISSING_VALUE_EXCEPTION, colorToFloat32Array, gpbToFloat32Array, gpbToVector3, verticesToFloat32Array } from "../common-utilities";
import { modeling } from "@/proto-bundle";
import { ColorEntity } from "@/api/schema";


test( "gpbToVector3", () => {

    expect( () => gpbToVector3({x: undefined, y: 0, z: 0}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( () => gpbToVector3({x: 0, y: undefined, z: 0}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( () => gpbToVector3({x: 0, y: 0, z: undefined}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( gpbToVector3({x: 0, y: 0, z: 0}) ).toStrictEqual( new Vector3(0,0,0) );
    expect( gpbToVector3({x: 0.5, y: 100, z: 0.00001}) ).toStrictEqual( new Vector3(0.5, 100, 0.00001 ) );
} );

test( "gpbToFloat32Array", () => {

    expect( () => gpbToFloat32Array({x: undefined, y: 0, z: 0}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( () => gpbToFloat32Array({x: 0, y: undefined, z: 0}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( () => gpbToFloat32Array({x: 0, y: 0, z: undefined}) ).toThrow(MISSING_VALUE_EXCEPTION);
    expect( gpbToFloat32Array({x: 0, y: 0, z: 0}) ).toStrictEqual( new Float32Array( [ 0, 0, 0 ] ) );
    expect( gpbToFloat32Array({x: 0.5, y: 100, z: 0.00001}) ).toStrictEqual( new Float32Array( [ 0.5, 100, 0.00001 ] ) );
} );

test( "verticesToFloat32Array", () => {

    const vertices: modeling.IVertex[] = [ { x: 0, y: 1, z: 2 }, { x: 3, y: 4, z: 5 }, {x: 6, y: 7, z: 8}];

    expect( verticesToFloat32Array( vertices ) ).toStrictEqual( new Float32Array( [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ] ) );
} );

test( "colorToFloat32Array", () => {

    const colorEntity: ColorEntity = {
         id: 0,
         name: "color",
         rgb: "ffff00",
         isTrans: false,
         numParts: 0,
         numSets: 0,
         startYear: 0,
         endYear: 0
    }

    expect( colorToFloat32Array( colorEntity, -1 ) ).toStrictEqual( new Float32Array( [] ) );
    expect( colorToFloat32Array( colorEntity, 0 ) ).toStrictEqual( new Float32Array( [] ) );
    expect( colorToFloat32Array( colorEntity, 3 ) ).toStrictEqual( new Float32Array( [
        1, 1, 0,
        1, 1, 0,
        1, 1, 0
    ] ) );
} );