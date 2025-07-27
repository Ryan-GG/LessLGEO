"use client"
import * as React from "react"
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "@/components/ui/navigation-menu"
import Image from "next/image"
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar"

export function AppNavBar() {
  return (
    <div className="columns-6">
        <Image
            src="/favicon.ico"
            alt="Place holder icon"
            width={50}
            height={50}       
        />
        <h1 className="">LessLGEO</h1>
        <NavigationMenu 
            className=""
            viewport={false}
        >
        <NavigationMenuList>
            <NavigationMenuItem>
            <NavigationMenuTrigger>Home</NavigationMenuTrigger>
            </NavigationMenuItem>
            <NavigationMenuItem>
            <NavigationMenuTrigger>Models</NavigationMenuTrigger>
            </NavigationMenuItem>
            <NavigationMenuItem>
            <NavigationMenuTrigger>Editor</NavigationMenuTrigger>
            </NavigationMenuItem>
            <NavigationMenuItem>
            <NavigationMenuTrigger>Discover</NavigationMenuTrigger>
            </NavigationMenuItem>
        </NavigationMenuList>
        </NavigationMenu>
        <Avatar className="rounded-lg">
            <AvatarImage 
                src="/favicon.ico"
                alt="Avatar Icon"
                width={50}
                height={50}
            />
            <AvatarFallback>LL</AvatarFallback>
        </Avatar>
    </div>
  )
}

