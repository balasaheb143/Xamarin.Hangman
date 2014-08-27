using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using Android.App;
using Android.Runtime;
using SharedCode.Models;
using SQLite;
using Environment = Android.OS.Environment;

namespace Fundora.Hangman.Application
{
    [Application(Debuggable = true)]
    public class MyApplication : Android.App.Application
    {
        private static String RomanianDbName = "Fundora.Hangman.Romanian.db3";
        private static String DbPath;

        public MyApplication(IntPtr javaReference, JniHandleOwnership transfer)
            : base(javaReference, transfer)
        {
        }

        public static SQLiteAsyncConnection sqLConnection { get; private set; }
        public static List<Word> Words { get; set; }

        public static List<Difficulty> DifficultyLevels { get; set; }

        public override void OnCreate()
        {
            try
            {
                base.OnCreate();
                InitializeDB();
                // do application specific things here
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private async Task InitializeDB()
        {
            try
            {
                DbPath = Path.Combine(Environment.ExternalStorageDirectory.ToString(), RomanianDbName);

                //if (!File.Exists(DbPath))
                //{
                using (var br = new BinaryReader(Assets.Open(RomanianDbName)))
                {
                    using (var bw = new BinaryWriter(new FileStream(DbPath, FileMode.Create)))
                    {
                        var buffer = new byte[2048];
                        int len = 0;
                        while ((len = br.Read(buffer, 0, buffer.Length)) > 0)
                        {
                            bw.Write(buffer, 0, len);
                        }
                    }
                }
                //}
                sqLConnection = new SQLiteAsyncConnection(DbPath, true);
                await ReadDB();
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        private async Task ReadDB()
        {
            try
            {
                Words = await sqLConnection.Table<Word>().ToListAsync();
                DifficultyLevels = await sqLConnection.Table<Difficulty>().ToListAsync();
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }
    }
}