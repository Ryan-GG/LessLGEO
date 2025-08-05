import { Vector3 } from "three";

export const PLATE_Y_TO_LDU = 8 as const;
export const BRICK_TO_LDU = 20 as const;
export const HALF_BRICK_TO_LDU = 10 as const;
export const BRICK_Y_TO_LDU = 24 as const;
export const STUD_HEIGHT = 4 as const;
export const STUD_DIAMETER = 12 as const;

export const LDU_TO_THREE_SCALAR_VECTOR = new Vector3( 1 / BRICK_TO_LDU, 1 / BRICK_Y_TO_LDU, 1 / BRICK_TO_LDU );