"use client";
import { modeling } from "@/proto-bundle";
import { ReactElement, ReactNode } from "react";
import { Quadrilateral } from "./quadrilateral";
import { Triangle } from "./triangle";

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode[] 
{
	if( gpb === undefined ) return [];
	return extractMeshes(gpb);
}

function extractMeshes(model: modeling.IModel | null | undefined): ReactNode[] {
	if (!model) return [];
  
	const meshes: ReactNode[] = [];
  
	const quadMeshes = model.quadrilateral?.map((quad) => (
	  <Quadrilateral key={`quad-${crypto.randomUUID()}`} gpb={quad} />
	)) ?? [];
  
	const triangleMeshes = model.triangle?.map((triangle) => (
	  <Triangle key={`tri-${crypto.randomUUID()}`} gpb={triangle} />
	)) ?? [];
  
	meshes.push(...quadMeshes, ...triangleMeshes);
  
	const childMeshes = model.piece?.flatMap((child) =>
	  extractMeshes(child.subModel)
	) ?? [];
  
	meshes.push(...childMeshes);
  
	return meshes;
  }
  