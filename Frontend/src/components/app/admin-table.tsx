import { PageHeader } from "@/components/common/page";
import { DataCard } from "@/components/app/list-primitives";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

export function AdminTablePage({
  title,
  description,
  columns,
  rows,
}: {
  title: string;
  description: string;
  columns: string[];
  rows: (string | number)[][];
}) {
  return (
    <div className="space-y-5">
      <PageHeader title={title} description={description} />
      <DataCard>
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                {columns.map((col) => (
                  <TableHead key={col}>{col}</TableHead>
                ))}
              </TableRow>
            </TableHeader>
            <TableBody>
              {rows.map((row, i) => (
                <TableRow key={i}>
                  {row.map((cell, j) => (
                    <TableCell key={j} className={j === 0 ? "font-medium" : ""}>
                      {cell}
                    </TableCell>
                  ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </DataCard>
    </div>
  );
}
