"use client"
import * as React from "react"
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "@/components/ui/navigation-menu"
import Image from "next/image"
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar"

export function AppNavBar() {
  return (
    <nav className="w-full bg-background/80 border-b border-border shadow-sm sticky top-0 z-40">
      <div className="flex items-center justify-between h-16 px-6 w-full">
        <div className="flex items-center gap-3 min-w-[120px]">
          <Image
            src="/favicon.ico"
            alt="Place holder icon"
            width={40}
            height={40}
          />
          <h1 className="font-bold text-lg tracking-tight">LessLGEO</h1>
        </div>
        <div className="flex-1 flex justify-center">
          <NavigationMenu className="bg-transparent shadow-none" viewport={false}>
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
        </div>
        <div className="flex items-center gap-4">
          <Avatar>
            <AvatarImage src="/avatar-placeholder.png" alt="User avatar" />
            <AvatarFallback>U</AvatarFallback>
          </Avatar>
        </div>
      </div>
    </nav>
  )
}

