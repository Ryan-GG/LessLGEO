import { AppNavBar } from "@/components/home/app-nav-bar";
import { Skeleton } from "@/components/ui/skeleton";

/**
 * Landing page for LessLGEO
 * Hero card content, with examples
 */
export default function Home() {
  return (
    <div className="flex flex-col min-h-screen bg-black">
      <header className="px-8">
        <AppNavBar/>
      </header>
      <main className="flex-1 pb-8 px-8 pt-2 flex flex-col gap-2">
        <Skeleton className="h-full w-full p-8 ">
          <Skeleton className="h-[50vh] w-1/3 bg-black"/>
        </Skeleton>
        <Skeleton className="h-full w-full p-8 grid grid-cols-6 gap-4">
          <Skeleton className="h-50 w-50 bg-black"/>
          <Skeleton className="h-50 w-50 bg-black"/>
          <Skeleton className="h-50 w-50 bg-black"/>
          <Skeleton className="h-50 w-50 bg-black"/>
          <Skeleton className="h-50 w-50 bg-black"/>
          <Skeleton className="h-50 w-50 bg-black"/>
        </Skeleton>
      </main>
      <footer className="p-8">
        <Skeleton className="h-8 w-full"/>
      </footer>
    </div>
  );
}
