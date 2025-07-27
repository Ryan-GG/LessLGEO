import { AppNavBar } from "@/components/home/app-nav-bar";
import { Skeleton } from "@/components/ui/skeleton";
import Image from "next/image";

export default function Home() {
  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 bg-black">
      <header>
        <Skeleton className="h-8 w-7xl"/>
        <AppNavBar/>
      </header>
      <main>
        <Skeleton className="h-150 w-7xl item-center justify-items-center z-0">
          <Skeleton className="h-125 w-6xl z-1 bg-black"/>
        </Skeleton>
      </main>
      <footer className="row-start-3 flex gap-[24px] flex-wrap items-center justify-center">
        <Skeleton className="h-8 w-7xl"/>
      </footer>
    </div>
  );
}
