export default function Logo() {
  return (
    <div className="flex items-center gap-2 select-none">
      <div className="h-8 w-8 rounded-xl bg-gradient-to-br from-indigo-500 to-fuchsia-500 shadow" />
      <span className="text-xl font-bold tracking-tight">
        DevMatch<span className="text-fuchsia-500">.AI</span>
      </span>
    </div>
  );
}
