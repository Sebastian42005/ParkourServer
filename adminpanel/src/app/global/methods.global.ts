export function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

export function instantToReadableConverter(instant: string): string {
  const datePart = instant.split('T')[0];
  const timePart = instant.split('T')[1];

  const dateSplit = datePart.split("-");
  const timeSplit = timePart.split(":");

  return `${dateSplit[2]}.${dateSplit[1]}.${dateSplit[0]} ${timeSplit[0]}:${timeSplit[1]}`

}
