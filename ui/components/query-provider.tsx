"use client";

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactElement, ReactNode, useState } from 'react';

export default function QueryProviders( { children } : {children: ReactNode } ): ReactElement
{
	const [ queryClient ] = useState( () => new QueryClient() );

	return (
		<QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
	);
}