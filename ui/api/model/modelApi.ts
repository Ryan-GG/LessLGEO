
export async function fetchAllModelIds(): Promise<string[]> {
    const response = await fetch('http://localhost:8080/api/model/v1/ids');
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  }