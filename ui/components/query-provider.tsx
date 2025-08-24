"use client";

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactElement, ReactNode } from 'react';

/**
 * 
 * @returns Wrapper for children components to allow usage of the query client. The query client is used
 * to easily fetch and load data with caching options. 
 * See more at `https://tanstack.com/query/latest/docs/framework/react/overview`
 */
export default function QueryProvider( { children } : {children: ReactNode } ): ReactElement
{
	const queryClient = new QueryClient();

	return (
		<QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
	);
}